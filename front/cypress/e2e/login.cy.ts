describe('Login spec', () => {
  it('Login successfully with correct credentials should lead to sessions', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //Then
    cy.url().should('include', '/sessions');
    cy.get('.error').should('not.exist');
  });

  it('Login fails with incorrect credentials', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
    });

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //Then
    cy.url().should('include', '/login');
    cy.get('.error').should('exist');
  });

  it('button should be enabled/disabled according to what is typed', () => {
    cy.visit('/login');
    cy.get('[data-test-id="login-submit"]').should('be.disabled');

    cy.get('input[formControlName=email]').type('yoga');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('[data-test-id="login-submit"]').should('be.disabled');

    cy.get('input[formControlName=email]').type('@studio.com');
    cy.get('[data-test-id="login-submit"]').should('not.be.disabled');

    cy.get('input[formControlName=password]').clear();
    cy.get('[data-test-id="login-submit"]').should('be.disabled');
  });

  it('Logout successfully lead back to login page', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //When
    cy.get('[data-test-id="navbar-me"]').click();

    //Then
    cy.url().should('not.contain', '/sessions');
  });
});
