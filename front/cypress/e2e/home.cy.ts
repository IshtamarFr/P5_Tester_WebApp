describe('Login spec', () => {
  it('home successfully', () => {
    cy.visit('');
    cy.title().should('eq', 'Yoga-app');
  });

  it('click on Login should redirect to login', () => {
    //TODO: to continue here
  });
});
