describe('Sessions modify spec', () => {
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

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'First try',
        date: '1902-01-01T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'Yoga could be some cool activity to try',
        users: [1],
        createdAt: '2023-09-30T16:40:26',
        updatedAt: '2023-09-30T16:41:53',
      }
    ).as('session-detail');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2023-09-30T15:23:53',
          updatedAt: '2023-09-30T15:23:53',
        },
      ]
    ).as('teachers');

    cy.intercept(
      {
        method: 'PUT',
        url: '/api/session/1',
      },
      {
        statusCode: 200,
      }
    ).as('teacher');

    //When
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.get('.edit-button').should('exist');
    cy.get('.edit-button').click();

    cy.get('textarea[formControlName=description]').type(
      ' as in mock description'
    );
    cy.get('[data-test-id="session-submit"]').should('be.enabled');

    cy.get('input[formControlName=name]').clear();
    cy.get('[data-test-id="session-submit"]').should('be.disabled');
    cy.get('input[formControlName=name]').type('Super session de yoga');

    cy.get('input[formControlName=date]').clear();
    cy.get('[data-test-id="session-submit"]').should('be.disabled');
    cy.get('input[formControlName=date]').type('2034-11-10');

    cy.get('[data-test-id="session-submit"]').click();

    //Then
    cy.url().should('contain', '/sessions');
  });
});
