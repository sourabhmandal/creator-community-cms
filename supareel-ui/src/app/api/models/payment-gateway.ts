/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

export interface PaymentGateway {
  amount?: number;
  createdAt?: string;
  currency?: 'USD' | 'EUR';
  id?: string;
  providerName?: 'STRIPE' | 'PAYPAL';
  status?: 'SUCCESSFUL' | 'FAILED';
  transactionId?: string;
}