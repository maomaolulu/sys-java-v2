package com.ruoyi.admin.domain.poi;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 付款通知书 明细表格的自定义渲染策略<br/>
 * 1. 填充货品数据 <br/>
 * 2. 填充人工费数据 <br/>
 *
 * @author zh
 */
@SuppressWarnings("unchecked")
public class QuotationFinalTablePolicy extends DynamicTableRenderPolicy {

    // 内容填充数据所在行数
    int goodsStartRow = 1;


    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) {
            return;
        }
        List<RowRenderData> goods = (List<RowRenderData>) data;


        if (null != goods) {

            for (int i = 0; i < goods.size(); i++) {
                //每次循环生成行
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(goodsStartRow + i);
                //每次循环生成行内表格
                for (int j = 0; j < 9; j++) {
                    insertNewTableRow.createCell();
                }
                TableRenderPolicy.Helper.renderRow(table.getRow(goodsStartRow + i), goods.get(i));
            }

        }



        //合并单元格封装

        //列一
        ces(table,goods,1,null,1);
        //列二
        ArrayList<Integer> integer2 = new ArrayList<>();
        integer2.add(2);
        integer2.add(3);
        ces(table,goods,2,integer2,1);
        //列三
        ArrayList<Integer> integers3 = new ArrayList<>();
        integers3.add(2);
        integers3.add(3);
        integers3.add(4);
        ces(table,goods,3,integers3,1);

    }

    /**
     *
     * @param table 表格
     * @param data  数据
     * @param col   合并列数
     * @param cols  根据那列合并（可多列）
     * @param StartRow 合并表格起始行
     * @throws Exception
     */
    private void ces(XWPFTable table, List<RowRenderData> data, Integer col, List<Integer> cols,Integer StartRow )  throws Exception{


        if (cols != null && cols.size() > 0) {
            //需要合并的内容
            List<String> list = new ArrayList<>();
                for (int j = 0; j < data.size(); j++) {

                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < cols.size(); i++) {
                        String s = data.get(j).getCells().get(i).getParagraphs().get(0).getContents().get(0).toString();
                        stringBuilder.append(s+"-");
                    }
                    //合并的内容
                    list.add(stringBuilder.toString());

                }

            //筛选出不同的数据重新放在新list中
            List<String> collect1 = list.stream().distinct().collect(Collectors.toList());
            //需要合并的内容 及次数
            Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

            //第2列次数num
            for (String s : collect1) {
                //每统计完一个，需要重置num
                int num = 0;

                for (int i = 0; i < data.size(); i++) {

                    StringBuilder stringBuilder = new StringBuilder();

                    for (int j = 0; j < cols.size(); j++) {
                        String s1 = data.get(i).getCells().get(j).getParagraphs().get(0).getContents().get(0).toString();
                        stringBuilder.append(s1+"-");
                    }
                    if (s.equals(stringBuilder.toString())) {
                        num++;
                    }
                }

                linkedHashMap.put(s, num);

            }
            for (String key:linkedHashMap.keySet()){
                System.err.println(key+"---"+linkedHashMap.get(key));
            }
            //开始合并
            for (String s : collect1) {
                //从我们map中取出刚才放入的单位的个数
                Integer change = Integer.parseInt(String.valueOf(linkedHashMap.get(s)));
                //开始循环数据
                //元素第一次出现的位置
                int firstLocation = list.lastIndexOf(s) + 1;
                int fromRow = firstLocation - change + StartRow;
                int toRow = list.lastIndexOf(s) + StartRow;

//由于TableTools.mergeCellsVertically中参数toRow要大于fromRow，否则会报错，所以num>1的才参与合并
                if (change > 1) {

                    TableTools.mergeCellsVertically(table, col, fromRow, toRow);
                }


            }


        } else {

            //需要合并的内容
            List<String> list = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {

                String typeName = data.get(i).getCells().get(col).getParagraphs().get(0).getContents().get(0).toString();
                list.add(typeName); //第3列（车间+岗位）

            }


            //筛选出不同的数据重新放在新list中
            List<String> collect1 = list.stream().distinct().collect(Collectors.toList());

            //需要合并的内容 及次数
            Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

            //第2列次数num
            for (String s : collect1) {
                //每统计完一个，需要重置num
                int num = 0;
                for (int i = 0; i < data.size(); i++) {
                    String typeName1 = data.get(i).getCells().get(col).getParagraphs().get(0).getContents().get(0).toString();
                    if (s.equals(typeName1)) {
                        num++;
                    }
                }
                linkedHashMap.put(s, num);

            }

            //开始合并
            for (String s : collect1) {
                //从我们map中取出刚才放入的单位的个数
                Integer change = Integer.parseInt(String.valueOf(linkedHashMap.get(s)));
                //开始循环数据
                //元素第一次出现的位置
                int firstLocation = list.lastIndexOf(s) + 1;
                int fromRow = firstLocation - change + StartRow;
                int toRow = list.lastIndexOf(s) + StartRow;
//由于TableTools.mergeCellsVertically中参数toRow要大于fromRow，否则会报错，所以num>1的才参与合并


                if (change > 1) {

                    TableTools.mergeCellsVertically(table, col, fromRow, toRow);
                }


            }


        }

    }


}
