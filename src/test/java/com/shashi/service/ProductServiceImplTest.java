package com.shashi.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.sql.SQLException;

import com.shashi.dao.ProductDAO;
import com.shashi.dao.ProductDetailDAO;
import com.shashi.entity.ProductEntity;
import com.shashi.service.exception.ProductException;
import com.shashi.testutil.ProductTestData;
import com.shashi.vo.ProductVO;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {
	@Mock
	private ProductDAO productDao;
	
	@Mock
	private ProductDetailDAO productDetailDao;
	
	@Spy //partial mock
	private ProductServiceImpl impl;
	private ProductVO input = null;
	private ProductEntity output = null;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		input = ProductTestData.createProductVO();
		output = ProductTestData.createProductEntity();
		
		when(impl.getDao()).thenReturn(productDao);
		when(impl.getDetailDao()).thenReturn(productDetailDao);		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSaveProductNullProductVO(){
		impl.saveProduct(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveProductNullProductName(){
		input.setProductName(null);
		impl.saveProduct(input);
	}
	
	@Test
	public void testCreateProduct() throws SQLException{
		when(productDao.createProduct(any(ProductEntity.class))).thenReturn(output);
		
		ProductVO result = impl.saveProduct(input);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(input.getProductName(),result.getProductName());
		assertEquals(input.getDescription(),result.getDescription());
		
		InOrder inorder = inOrder(productDao,productDetailDao);
		
		inorder.verify(productDao,times(1)).createProduct(any(ProductEntity.class));
		inorder.verify(productDetailDao, atLeast(1)).saveProductDetail(any(ProductEntity.class));
		//times
		//atLeast
		//atMost
		//timeout
		//never
		
		inorder.verify(productDao,never()).find(any(Long.class));
		inorder.verify(productDao,never()).updateProduct(any(ProductEntity.class));

	}
	
	@Test
	public void testUpdateProduct() throws SQLException{
		input.setId(new Long(2000));
		when(productDao.find(any(Long.class))).thenReturn(output);
		
		ProductVO result = impl.saveProduct(input);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(input.getProductName(),result.getProductName());
		assertEquals(input.getDescription(),result.getDescription());
		
		verify(productDao,never()).createProduct(any(ProductEntity.class));
		verify(productDetailDao, atLeast(1)).saveProductDetail(any(ProductEntity.class));	
		verify(productDao).find(any(Long.class));
		verify(productDao).updateProduct(any(ProductEntity.class));

	}
	
	@Test(expected=ProductException.class)
	public void testUpdateProductException() throws SQLException{
		input.setId(new Long(2000));
		when(productDao.find(any(Long.class))).thenThrow(new SQLException("The connection to BD failed"));
		
		ProductVO result = impl.saveProduct(input);
		
		
		verify(productDao).find(any(Long.class));

	}

}
