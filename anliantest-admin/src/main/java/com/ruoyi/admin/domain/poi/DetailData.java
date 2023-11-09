package com.ruoyi.admin.domain.poi;

import java.util.List;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;

@Data
public class DetailData {
    //报价导出
    private List<RowRenderData> quotationTable;

}
