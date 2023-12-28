package com.carsonlius.framework.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/27 16:20
 * @company
 * @description
 */
@Data
@AllArgsConstructor
public class KeyValue <K,V>{
    private K key;
    private V value;
}
