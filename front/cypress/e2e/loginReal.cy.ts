describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('[data-test-id="login-submit"]').click();

    cy.url().should('include', '/sessions');
  });
});
