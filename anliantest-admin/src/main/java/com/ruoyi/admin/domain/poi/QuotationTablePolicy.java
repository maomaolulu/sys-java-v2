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
 * @author Sayi
 */
@SuppressWarnings("unchecked")
public class QuotationTablePolicy extends DynamicTableRenderPolicy {

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
                for (int j = 0; j < 8; j++) {
                    insertNewTableRow.createCell();
                }
                TableRenderPolicy.Helper.renderRow(table.getRow(goodsStartRow + i), goods.get(i));
            }

        }

        //合并单元格 goods.get(i).getCells().get(1).getParagraphs().get(0).getContents().get(0).toString() //某行某列的值
        //goods.get(i)：某行 这边取所有循环  .getCells().get(1)： 某列 合并的列 这边取第2列需要合并  .getParagraphs().get(0).getContents().get(0)：值

//        //需要合并的内容 //第2列
//        List<String> list1 = new ArrayList<>();
//        //需要合并的内容 //第3列
//        List<String> list2 = new ArrayList<>();
//
//        for (int i = 0; i < goods.size(); i++) {
//            //第2列
//            String typeName = goods.get(i).getCells().get(1).getParagraphs().get(0).getContents().get(0).toString();
//
//
//            //第3列
//            String typeName2 = goods.get(i).getCells().get(2).getParagraphs().get(0).getContents().get(0).toString();
//            list1.add(typeName);//第一列 （车间）
//            list2.add(typeName + "-" + typeName2); //第3列（车间+岗位）
//
//        }
//        //筛选出不同的数据重新放在新list中 第2列
//        List<String> collect1 = list1.stream().distinct().collect(Collectors.toList());
//        //筛选出不同的数据重新放在新list中 第3列
//        List<String> collect2 = list2.stream().distinct().collect(Collectors.toList());
//        //统计不同数据在全部数据中出现的次数num，并整合放在map中，后面合并的行数需要用到num
//        //需要合并的内容 及次数
//        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
//
//        //第2列次数num
//        for (String s : collect1) {
//            //每统计完一个，需要重置num
//            int num = 0;
//            for (int i = 0; i < goods.size(); i++) {
//                String typeName1 = goods.get(i).getCells().get(1).getParagraphs().get(0).getContents().get(0).toString();
//                if (s.equals(typeName1)) {
//                    num++;
//                }
//            }
//            linkedHashMap.put(s, num);
//
//        }
//
//        //第3列次数num
//        for (String s : collect2) {
//            //每统计完一个，需要重置num
//            int num = 0;
//            for (int i = 0; i < goods.size(); i++) {
//                String typeName1 = goods.get(i).getCells().get(1).getParagraphs().get(0).getContents().get(0).toString();
//                String typeName2 = goods.get(i).getCells().get(2).getParagraphs().get(0).getContents().get(0).toString();
//
//                if (s.equals(typeName1 + "-" + typeName2)) {
//                    num++;
//                }
//
//            }
//
//            linkedHashMap.put(s, num);
//        }
//
//
//        //开始合并2列
//        for (String s : collect1) {
//            //从我们map中取出刚才放入的单位的个数
//            Integer change = Integer.parseInt(String.valueOf(linkedHashMap.get(s)));
//            //开始循环数据
//            //元素第一次出现的位置
//            int firstLocation = list1.lastIndexOf(s) + 1;
//            int fromRow = firstLocation - change + 1;
//            int toRow = list1.lastIndexOf(s) + 1;
////由于TableTools.mergeCellsVertically中参数toRow要大于fromRow，否则会报错，所以num>1的才参与合并
//
//
//            if (change > 1) {
//
//                TableTools.mergeCellsVertically(table, 1, fromRow, toRow);
//            }
//
//
//        }
//
//
//        //开始合并3列
//        for (String s : collect2) {
//            //从我们map中取出刚才放入的单位的个数
//            Integer change = Integer.parseInt(String.valueOf(linkedHashMap.get(s)));
//            //开始循环数据
//            //元素第一次出现的位置
//            int firstLocation = list2.lastIndexOf(s) + 1;
//            int fromRow = firstLocation - change + 1;
//            int toRow = list2.lastIndexOf(s) + 1;
////由于TableTools.mergeCellsVertically中参数toRow要大于fromRow，否则会报错，所以num>1的才参与合并
//
//            if (change > 1) {
//
//                TableTools.mergeCellsVertically(table, 2, fromRow, toRow);
//            }
//
//
//        }

        //合并单元格封装
        ArrayList<Integer> integers = new ArrayList<>();

        integers.add(2);
        integers.add(3);

        ces(table,goods,1,null,1);
        ces(table,goods,2,integers,1);

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
