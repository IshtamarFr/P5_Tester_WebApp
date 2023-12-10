describe('Routing spec', () => {
  it('should show correct session data after it is clicked on as well as delete button if logged as admin', () => {
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
        url: '/api/teacher/1',
      },
      {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: '2023-09-30T15:23:53',
        updatedAt: '2023-09-30T15:23:53',
      }
    ).as('teacher');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //When
    cy.get('.detail-button').click();

    //Then
    cy.get('.mat-card-title').contains('First');
    cy.get('.ml1').contains('January 1, 1902');
    cy.get('.ml1').contains('DELAHAYE');
    cy.get('.description').contains('Yoga could be some cool activity to try');
    cy.get('.ml1').contains('Delete');
  });

  it('should show correct session data after it is clicked on but not delete button if logged as non admin', () => {
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
        url: '/api/teacher/1',
      },
      {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: '2023-09-30T15:23:53',
        updatedAt: '2023-09-30T15:23:53',
      }
    ).as('teacher');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    //When
    cy.get('.detail-button').click();

    //Then
    cy.get('.mat-card-title').contains('First');
    cy.get('.ml1').contains('January 1, 1902');
    cy.get('.ml1').contains('DELAHAYE');
    cy.get('.description').contains('Yoga could be some cool activity to try');
    cy.get('.ml1').should('not.contain', 'Delete');
  });
});
