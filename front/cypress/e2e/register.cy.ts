describe('Register spec', () => {
  it('Register successfully with correct credentials should lead to sessions', () => {
    //Given
    cy.intercept('POST', '/api/auth/register', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    //When
    cy.visit('/register');

    cy.get('input[formControlName=firstName]').type('admin');
    cy.get('input[formControlName=lastName]').type('admin');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('[data-test-id="register-submit"]').click();

    //Then
    cy.url().should('include', '/login');
  });
});
