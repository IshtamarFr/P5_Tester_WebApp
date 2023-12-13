describe('Sessions spec', () => {
  it('should show sessions after logged in', () => {
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
      [
        {
          id: 1,
          name: 'First try',
          date: '2023-09-30T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'Yoga could be some cool activity to try',
          users: [],
          createdAt: '2023-09-30T16:40:26',
          updatedAt: '2023-09-30T16:41:53',
        },
        {
          id: 2,
          name: 'Second try',
          date: '2023-10-17T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'I love yoga',
          users: [],
          createdAt: '2023-10-30T16:40:26',
          updatedAt: '2023-10-30T16:41:53',
        },
      ]
    ).as('session');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //Then
    cy.get('.mat-card-title').contains('First');
    cy.get('.mat-card-title').contains('Second');
  });

  it('should show Create, Edit and Detail after logged in as admin', () => {
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
      [
        {
          id: 1,
          name: 'First try',
          date: '2023-09-30T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'Yoga could be some cool activity to try',
          users: [],
          createdAt: '2023-09-30T16:40:26',
          updatedAt: '2023-09-30T16:41:53',
        },
      ]
    ).as('session');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //Then
    cy.get('.create-button').should('exist');
    cy.get('.detail-button').should('exist');
    cy.get('.edit-button').should('exist');
  });

  it('should hide Create and Edit but show Detail after logged in as non admin', () => {
    //Given
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'First try',
          date: '2023-09-30T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'Yoga could be some cool activity to try',
          users: [],
          createdAt: '2023-09-30T16:40:26',
          updatedAt: '2023-09-30T16:41:53',
        },
      ]
    ).as('session');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //Then
    cy.get('.create-button').should('not.exist');
    cy.get('.detail-button').should('exist');
    cy.get('.edit-button').should('not.exist');
  });
});
