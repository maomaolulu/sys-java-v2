package com.ruoyi.system.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.common.entity.SysDictData;
import com.ruoyi.system.common.mapper.SysDictDataMapper;
import com.ruoyi.system.common.service.SysDictDataService;
import org.springframework.stereotype.Service;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService  {
//    @Autowired
//    private RedisService redis;
//    @Autowired
//    private SysDictDataMapper dictDataMapper;
//
//    /**
//     * 根据条件分页查询字典数据
//     *
//     * @param dictData 字典数据信息
//     * @return 字典数据集合信息
//     */
//    @Override
//    public List<SysDictData> selectDictDataList(SysDictData dictData)
//    {
//        List<SysDictData> sysDictData = dictDataMapper.selectDictDataList(dictData);
//        return sysDictData;
//    }
//
//    private void dictCache() {
//        List<SysDictData> sysDictData1 = dictDataMapper.selectAll();
//        sysDictData1.stream().forEach(d-> {
//            redis.setCacheObject("dict:"+d.getDictType()+":"+d.getDictValue(),d.getDictLabel());
//            if(d.getDictType().equals("assets_type")){
//                redis.setCacheObject("dict:"+d.getDictCode(),d.getDictLabel());
//            }
//        });
//    }
//
//    /**
//     * 根据字典类型查询字典数据
//     *
//     * @param dictType 字典类型
//     * @return 字典数据集合信息
//     */
//    @Override
//    public List<SysDictData> selectDictDataByType(String dictType)
//    {
//        return dictDataMapper.selectDictDataByType(dictType);
//    }
//
//    /**
//     * 根据字典类型和字典键值查询字典数据信息
//     *
//     * @param dictType 字典类型
//     * @param dictValue 字典键值
//     * @return 字典标签
//     */
//    @Override
//    public String selectDictLabel(String dictType, String dictValue)
//    {
//        return dictDataMapper.selectDictLabel(dictType, dictValue);
//    }
//
//    /**
//     * 根据字典数据ID查询信息
//     *
//     * @param dictCode 字典数据ID
//     * @return 字典数据
//     */
//    @Override
//    public SysDictData selectDictDataById(Long dictCode)
//    {
//        return dictDataMapper.selectDictDataById(dictCode);
//    }
//
//    /**
//     * 通过字典ID删除字典数据信息
//     *
//     * @param dictCode 字典数据ID
//     * @return 结果
//     */
//    @Override
//    public int deleteDictDataById(Long dictCode)
//    {
//        return dictDataMapper.deleteDictDataById(dictCode);
//    }
//
//    /**
//     * 批量删除字典数据
//     *
//     * @param ids 需要删除的数据
//     * @return 结果
//     */
//    @Override
//    public int deleteDictDataByIds(String ids)
//    {
//        return dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
//    }
//
//    /**
//     * 新增保存字典数据信息
//     *
//     * @param dictData 字典数据信息
//     * @return 结果
//     */
//    @Override
//
//    public int insertDictData(SysDictData dictData)
//    {
//        int i = dictDataMapper.insertDictData(dictData);
//        dictCache();
//        return i;
//    }
//
//    /**
//     * 修改保存字典数据信息
//     *
//     * @param dictData 字典数据信息
//     * @return 结果
//     */
//    @Override
//    public int updateDictData(SysDictData dictData)
//    {
//        int i = dictDataMapper.updateDictData(dictData);
//        dictCache();
//        return i;
//    }
}
