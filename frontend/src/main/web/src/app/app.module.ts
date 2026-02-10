import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RestangularModule } from 'ngx-restangular';

import { AppComponent } from './app.component';

export function restangularConfigFactory(RestangularProvider: any) {
  RestangularProvider.setBaseUrl('http://localhost:8080');

  RestangularProvider.addResponseInterceptor(
      (data: any, operation: string, what: string) => {
        // Only rewrite responses for "getList" operations
        if (operation === 'getList') {
          // Example: GET /customers  ->  { _embedded: { customerList: [...] }, ... }
          const embedded = data?._embedded;

          // If the list key matches the collection name:
          // /customers -> customerList (your case)
          if (what === 'customers' && embedded?.customerList) return embedded.customerList;

          // Generic fallback: if there is exactly one embedded array, return it
          if (embedded && typeof embedded === 'object') {
            const arrays = Object.values(embedded).filter(Array.isArray);
            if (arrays.length === 1) return arrays[0];
          }
        }

        return data;
      }
  );
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RestangularModule.forRoot(restangularConfigFactory)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
