describe('Routing spec', () => {
  it('home successfully', () => {
    cy.visit('');
    cy.title().should('eq', 'Yoga-app');
  });

  it('click on Login should redirect to login', () => {
    cy.visit('');
    cy.get('[data-test-id="navbar-login"]').should('exist');
    cy.get('[data-test-id="navbar-login"]').click();
    cy.url().should('contain', 'login');
  });

  it('click on Register should redirect to register', () => {
    cy.visit('');
    cy.get('[data-test-id="navbar-register"]').should('exist');
    cy.get('[data-test-id="navbar-register"]').click();
    cy.url().should('contain', 'register');
  });
});
