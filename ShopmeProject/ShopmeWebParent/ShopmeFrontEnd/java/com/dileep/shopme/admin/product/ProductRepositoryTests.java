package com.dileep.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Brand;
import com.dileep.shopme.common.entity.Category;
import com.dileep.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace =Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private IProductRepository productRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 37l);
		
		Category category = entityManager.find(Category.class, 5l);
		
		Product product =  new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("Short description for Acer Aspire");
		product.setFullDescription("Full description for Acer Aspire");
		
		product.setBrand(brand);
		product.setCategory(category);
		product.setEnabled(true);
		product.setInStock(true);
		
		product.setPrice(678);
		product.setCost(600);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = productRepository.findAll();
		
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Long id = 2l;
		Product product = productRepository.findById(id).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Long id = 1l;
		Product product = productRepository.findById(id).get();
		product.setPrice(499);
		productRepository.save(product);
		
		Product updatedProduct = entityManager.find(Product.class, id);
		
		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	
	}
	
	@Test
	public void testDeleteProduct() {
		Long id= 3l;
		 
		productRepository.deleteById(id);
		
		Optional<Product> result = productRepository.findById(id);
		
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testSaveProductWithImages() {
		
		Long id = 1l;
		Product product = productRepository.findById(id).get();
		
		product.setMainImage("main_image.jpg");
		product.addExtraImage("extra_image1.jpg");
		product.addExtraImage("extra_image2.jpg");
		product.addExtraImage("extra_image3.jpg");
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}

	@Test
	public void testSaveProductWithDetails() {
		Long productId = 2l;
		Product product = productRepository.findById(productId).get();
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Android 10");		
		
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
	
}
