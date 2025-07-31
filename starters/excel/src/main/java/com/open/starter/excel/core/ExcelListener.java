package com.open.starter.excel.core;

import cn.idev.excel.read.listener.ReadListener;

/**
 * Excel 导入监听
 *
 * @author open
 */
public interface ExcelListener<T> extends ReadListener<T> {

    ExcelResult<T> getExcelResult();

}

