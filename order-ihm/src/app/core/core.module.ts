import { NgModule } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { tokenInterceptor } from './interceptors/token.interceptor';
import { loggerResponseInterceptor } from './interceptors/logger.response.interceptor';
import { loggerErrorInterceptor } from './interceptors/logger.error.interceptor';
import { spinnnerInterceptor } from './interceptors/spinner.interceptor';
import { NgxSpinnerModule } from 'ngx-spinner';
import { SpinnerComponent } from './components/spinner/spinner.component';



@NgModule({
  declarations: [
    HeaderComponent,
    SpinnerComponent
  ],
  imports: [
    RouterModule,
    SharedModule,
    HttpClientModule,
    NgxSpinnerModule
  ],
  exports: [
    HeaderComponent,
    SpinnerComponent
  ],
  providers: [
    
    provideHttpClient(
      withInterceptors([tokenInterceptor, loggerResponseInterceptor, loggerErrorInterceptor, spinnnerInterceptor])
    )
  ]
})
export class CoreModule { }
