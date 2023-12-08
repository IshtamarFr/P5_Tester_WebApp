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
    cy.get('p').should('not.exist');
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
    cy.get('p').should('have.class', 'error');
  });
});
