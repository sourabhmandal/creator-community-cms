/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createProduct1 } from '../fn/product/create-product-1';
import { CreateProduct1$Params } from '../fn/product/create-product-1';
import { CreateProductResponse } from '../models/create-product-response';
import { findAllProducts1 } from '../fn/product/find-all-products-1';
import { FindAllProducts1$Params } from '../fn/product/find-all-products-1';
import { findAllProductsByOwner } from '../fn/product/find-all-products-by-owner';
import { FindAllProductsByOwner$Params } from '../fn/product/find-all-products-by-owner';
import { findProductById } from '../fn/product/find-product-by-id';
import { FindProductById$Params } from '../fn/product/find-product-by-id';
import { PageResponseProductByIdResponse } from '../models/page-response-product-by-id-response';
import { ProductByIdResponse } from '../models/product-by-id-response';

@Injectable({ providedIn: 'root' })
export class ProductService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findAllProducts1()` */
  static readonly FindAllProducts1Path = '/';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllProducts1()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllProducts1$Response(params?: FindAllProducts1$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseProductByIdResponse>> {
    return findAllProducts1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllProducts1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllProducts1(params?: FindAllProducts1$Params, context?: HttpContext): Observable<PageResponseProductByIdResponse> {
    return this.findAllProducts1$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseProductByIdResponse>): PageResponseProductByIdResponse => r.body)
    );
  }

  /** Path part for operation `createProduct1()` */
  static readonly CreateProduct1Path = '/';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createProduct1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createProduct1$Response(params: CreateProduct1$Params, context?: HttpContext): Observable<StrictHttpResponse<CreateProductResponse>> {
    return createProduct1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createProduct1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createProduct1(params: CreateProduct1$Params, context?: HttpContext): Observable<CreateProductResponse> {
    return this.createProduct1$Response(params, context).pipe(
      map((r: StrictHttpResponse<CreateProductResponse>): CreateProductResponse => r.body)
    );
  }

  /** Path part for operation `findProductById()` */
  static readonly FindProductByIdPath = '/{product-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findProductById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findProductById$Response(params: FindProductById$Params, context?: HttpContext): Observable<StrictHttpResponse<ProductByIdResponse>> {
    return findProductById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findProductById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findProductById(params: FindProductById$Params, context?: HttpContext): Observable<ProductByIdResponse> {
    return this.findProductById$Response(params, context).pipe(
      map((r: StrictHttpResponse<ProductByIdResponse>): ProductByIdResponse => r.body)
    );
  }

  /** Path part for operation `findAllProductsByOwner()` */
  static readonly FindAllProductsByOwnerPath = '/owner';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllProductsByOwner()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllProductsByOwner$Response(params?: FindAllProductsByOwner$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseProductByIdResponse>> {
    return findAllProductsByOwner(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllProductsByOwner$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllProductsByOwner(params?: FindAllProductsByOwner$Params, context?: HttpContext): Observable<PageResponseProductByIdResponse> {
    return this.findAllProductsByOwner$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseProductByIdResponse>): PageResponseProductByIdResponse => r.body)
    );
  }

}