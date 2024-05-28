package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());

        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No product with given id"));

         product.setTitle(productDto.getTitle());
         product.setDescription(productDto.getDescription());
         product.setPrice((productDto.getPrice()));
         product.setDiscountedPrice(productDto.getDiscountedPrice());
         product.setQuantity(productDto.getQuantity());
         product.setLive(productDto.isLive());
         product.setStock(productDto.isStock());

        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No product with given id"));
        productRepository.delete(product);
    }
    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No product with given id"));
        return mapper.map(product, ProductDto.class);
    }
    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy)).ascending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepository.findAll(pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
       // productRepository.findAll(pageable);

        return pageableResponse;

    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy)).ascending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepository.findByLiveTrue(pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        productRepository.findAll(pageable);

        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy)).ascending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        productRepository.findAll(pageable);

        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with id not present"));
        Product product = mapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
      Product product =   productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("id not found"));
     Category category =  categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category of given id not found"));
      product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category =  categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category of given id not found"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy)).ascending() ;
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page =   productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);

    }
}
