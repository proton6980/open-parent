package com.open.extend.manager.function.user.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.open.common.core.pojo.R;
import com.open.common.core.utils.StringUtils;
import com.open.common.core.utils.file.MimeTypeUtils;
import com.open.extend.manager.function.user.domain.bo.SysUserBo;
import com.open.extend.manager.function.user.domain.bo.SysUserPasswordBo;
import com.open.extend.manager.function.user.domain.bo.SysUserProfileBo;
import com.open.extend.manager.function.user.domain.vo.AvatarVo;
import com.open.extend.manager.function.user.domain.vo.ProfileVo;
import com.open.extend.manager.function.user.domain.vo.SysUserVo;
import com.open.extend.manager.function.user.service.ISysUserService;
import com.open.starter.mybatisplus.helper.DataPermissionHelper;
import com.open.starter.satoken.annotation.Log;
import com.open.starter.satoken.annotation.RepeatSubmit;
import com.open.starter.satoken.enums.BusinessType;
import com.open.starter.satoken.utils.LoginHelper;
import com.open.starter.web.annotation.ApiEncrypt;
import com.open.starter.web.core.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 个人信息 业务处理
 *
 * @author open
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BaseController {

    private final ISysUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public R<ProfileVo> profile() {
        SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
        ProfileVo profileVo = new ProfileVo();
        profileVo.setUser(user);
        profileVo.setRoleGroup(userService.selectUserRoleGroup(user.getId()));
        profileVo.setPostGroup(userService.selectUserPostGroup(user.getId()));
        return R.ok(profileVo);
    }

    /**
     * 修改用户信息
     */
    @RepeatSubmit
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> updateProfile(@Validated @RequestBody SysUserProfileBo profile) {
        SysUserBo user = BeanUtil.toBean(profile, SysUserBo.class);
        user.setId(LoginHelper.getUserId());
        String username = LoginHelper.getUsername();
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return R.fail("修改用户'" + username + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return R.fail("修改用户'" + username + "'失败，邮箱账号已存在");
        }
        int rows = DataPermissionHelper.ignore(() -> userService.updateUserProfile(user));
        if (rows > 0) {
            return R.ok();
        }
        return R.fail("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     *
     * @param bo 新旧密码
     */
    @RepeatSubmit
    @ApiEncrypt
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<Void> updatePwd(@Validated @RequestBody SysUserPasswordBo bo) {
        SysUserVo user = userService.selectUserById(LoginHelper.getUserId());
        String password = user.getPassword();
        if (!BCrypt.checkpw(bo.getOldPassword(), password)) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(bo.getNewPassword(), password)) {
            return R.fail("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(user.getId(), BCrypt.hashpw(bo.getNewPassword())) > 0) {
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     *
     * @param avatarfile 用户头像
     */
    @RepeatSubmit
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<AvatarVo> avatar(@RequestPart("avatarfile") MultipartFile avatarfile) {
        if (!avatarfile.isEmpty()) {
            String extension = FileUtil.extName(avatarfile.getOriginalFilename());
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
                throw new RuntimeException("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
            }
//            SysOssVo oss = ossService.upload(avatarfile);
//            String avatar = oss.getUrl();
//            if (userService.updateUserAvatar(LoginHelper.getUserId(), oss.getOssId())) {
//                AvatarVo avatarVo = new AvatarVo();
//                avatarVo.setImgUrl(avatar);
//                return R.ok(avatarVo);
//            }
        }
        throw new RuntimeException("上传图片异常，请联系管理员");
    }
}
