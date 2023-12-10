describe('Me spec', () => {
  it('User should be able to get their own data', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'Scp999',
        firstName: 'Tickle',
        lastName: 'Monster',
        admin: false,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'Mar',
        firstName: 'Ishta',
        admin: false,
        createdAt: '2023-10-12T15:49:37',
        updatedAt: '2023-12-08T13:53:05',
      }
    ).as('me-details');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.get('[data-test-id="navbar-me"]').should('exist');
    cy.get('[data-test-id="navbar-me"]').click();

    //Then
    cy.get('p').contains('MAR');
    cy.get('p').contains('yoga@studio');
  });

  it('User should be able to get delete their own account', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'Scp999',
        firstName: 'Tickle',
        lastName: 'Monster',
        admin: false,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'Mar',
        firstName: 'Ishta',
        admin: false,
        createdAt: '2023-10-12T15:49:37',
        updatedAt: '2023-12-08T13:53:05',
      }
    ).as('me-details');

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
    });

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.get('[data-test-id="navbar-me"]').should('exist');
    cy.get('[data-test-id="navbar-me"]').click();

    cy.url().should('contain', 'me');
    cy.get('[data-test-id="account-delete-button"]').should('exist');
    cy.get('[data-test-id="account-delete-button"]').click();

    //Then
    cy.url().should('not.contain', 'me');
  });
});
