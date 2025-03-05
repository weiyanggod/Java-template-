package com.example.app.config;

import lombok.Data;
@Data
public class GenerateCode {
    /**
     * 生成规则设备编号:设备类型+五位编号（从1开始，不够前补0）
     *
     * @param equipmentType 设备类型
     * @param equipmentNo   最新设备编号
     * @return
     */
    public static String getNewEquipmentNo(String equipmentType, String equipmentNo) {
        // 默认一个初始设备编号
        String newEquipmentNo = equipmentType + "00001";
        // 判断传入的设备类型与最新设备编号不为空
        if (equipmentNo != null && !equipmentNo.isEmpty()) {
            // 字符串数字解析为整数
            int no = Integer.parseInt(equipmentNo);
            // 最新设备编号自增1
            int newEquipment = ++no;
            // 将整数格式化为5位数字
            newEquipmentNo = String.format(equipmentType + "%05d", newEquipment);
        }

        return newEquipmentNo;
    }

}
