package com.xichen.lumen.core

/**
 * Lumen 图片解密器接口
 * 
 * 用于对加密的图片字节数据进行解密
 * 
 * 设计原则：
 * - 核心库不关心具体解密算法
 * - 不落明文磁盘
 * - 支持自定义解密实现
 */
interface ImageDecryptor {
    /**
     * 解密图片数据
     * @param input 加密的字节数组
     * @return 解密后的字节数组
     */
    fun decrypt(input: ByteArray): ByteArray

    /**
     * 缓存 Key（用于区分不同的解密器）
     */
    val key: String
}

