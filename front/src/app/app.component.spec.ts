import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { routes } from './app-routing.module';
import { SessionService } from './services/session.service';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let app: AppComponent;
  let router: Router;

  const mockSessionService = {
    $isLogged(): Observable<boolean> {
      return of(true);
    },
    logOut(): void {},
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatToolbarModule,
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
      declarations: [AppComponent],
    }).compileComponents();
    const fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should check login state at beginning', () => {
    //Given
    let state!: boolean;

    //When
    app.$isLogged().subscribe((x) => (state = x));

    //Then
    expect(state).toBe(true);
  });

  it('should navigate to root when logOut', () => {
    //Given
    let navigateSpy = jest.spyOn(router, 'navigate');

    //When
    app.logout();

    //Then
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
