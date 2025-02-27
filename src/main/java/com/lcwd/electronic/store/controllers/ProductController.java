package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.impl.FileService;
import com.lcwd.electronic.store.services.impl.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){

        ProductDto productDto1 = productService.create(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto){

        ProductDto productDto1 = productService.update(productDto, productId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){
productService.delete(productId);
        ApiResponseMessage message =  ApiResponseMessage.builder().message("product Deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){

        ProductDto productDto = productService.get(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value="pageNumber",defaultValue="0",required=false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue="1",required=false)int pageSize,
            @RequestParam(value="sortBy",defaultValue="title",required=false)String sortBy,
            @RequestParam(value="sortDir",defaultValue="asc",required=false)String sortDir
    ){
        return new ResponseEntity<>(productService.getAll(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value="pageNumber",defaultValue="0",required=false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue="1",required=false)int pageSize,
            @RequestParam(value="sortBy",defaultValue="title",required=false)String sortBy,
            @RequestParam(value="sortDir",defaultValue="asc",required=false)String sortDir
    ){
        return new ResponseEntity<>(productService.getAllLive(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByTitleContaining(
           @PathVariable String query,
            @RequestParam(value="pageNumber",defaultValue="0",required=false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue="1",required=false)int pageSize,
            @RequestParam(value="sortBy",defaultValue="title",required=false)String sortBy,
            @RequestParam(value="sortDir",defaultValue="asc",required=false)String sortDir
    ){
        return new ResponseEntity<>(productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {

        String fileName = fileService.uploadFile(image,imagePath);
        ProductDto productDto = productService.get(productId);
       productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);

        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("product image is uploaded").status(HttpStatus.CREATED).success(true).build();
    return
            new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDto productDto = productService.get(productId);

        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
    }

}
