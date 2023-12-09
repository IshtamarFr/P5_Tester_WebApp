describe('Sessions spec', () => {
  it('Login successfully with correct credentials should lead to sessions', () => {
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
});
