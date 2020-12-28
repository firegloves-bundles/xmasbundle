import React from 'react';
import '@testing-library/jest-dom/extend-expect';
import { fireEvent, render, wait } from '@testing-library/react';
import 'i18n/__mocks__/i18nMock';
import dogMock from 'components/__mocks__/dogMocks';
import DogForm from 'components/DogForm';
import { createMuiTheme } from '@material-ui/core';
import { ThemeProvider } from '@material-ui/styles';

const theme = createMuiTheme();

describe('Dog Form', () => {
  it('shows form', () => {
    const { getByLabelText } = render(
      <ThemeProvider theme={theme}>
        <DogForm dog={dogMock} />
      </ThemeProvider>
    );
    expect(getByLabelText('entities.dog.name').value).toBe(
      'Rerum velit natus et consequatur qui delectus. Eum iure fuga ratione et deserunt modi cupiditate. Corporis dolores sit voluptatem. Nisi cum voluptas distinctio quos minus quia.'
    );
  });

  it('submits form', async () => {
    const handleSubmit = jest.fn();
    const { getByTestId } = render(
      <ThemeProvider theme={theme}>
        <DogForm dog={dogMock} onSubmit={handleSubmit} />
      </ThemeProvider>
    );

    const form = getByTestId('dog-form');
    fireEvent.submit(form);

    await wait(() => {
      expect(handleSubmit).toHaveBeenCalledTimes(1);
    });
  });
});
