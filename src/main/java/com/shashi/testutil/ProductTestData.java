package com.shashi.testutil;

import com.shashi.entity.ProductEntity;
import com.shashi.vo.ProductVO;

public class ProductTestData {
	public static ProductVO createProductVO(){
		ProductVO result = new ProductVO();
		result.setProductName("Atlec Speakers Dual");
		result.setDescription("Dual Speakers");
		return result;
	}
	
	public static ProductEntity createProductEntity(){
		ProductEntity result = new ProductEntity();
		result.setProductName("Atlec Speakers Dual");
		result.setDescription("Dual Speakers");
		result.setId(new Long(2000));
		return result;
	}
}
