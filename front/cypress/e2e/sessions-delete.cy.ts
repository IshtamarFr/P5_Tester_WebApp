describe('Session delete', () => {
  it('admin should be able to delete a session', () => {
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

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/1',
      },
      {
        statusCode: 200,
      }
    ).as('teacher');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );
    cy.get('.detail-button').click();
    cy.get('.ml1').contains('Delete');

    //When
    cy.get('.ml1').contains('Delete').click();

    //Then
    cy.url().should('include', '/sessions');
  });
});
