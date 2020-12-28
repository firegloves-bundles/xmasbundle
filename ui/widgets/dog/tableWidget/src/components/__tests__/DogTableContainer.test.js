import React from 'react';
import { render, wait } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';

import dogMocks from 'components/__mocks__/dogMocks';
import { apiDogsGet } from 'api/dogs';
import 'i18n/__mocks__/i18nMock';
import DogTableContainer from 'components/DogTableContainer';

jest.mock('api/dogs');

jest.mock('auth/withKeycloak', () => {
  const withKeycloak = (Component) => {
    return (props) => (
      <Component
        {...props} // eslint-disable-line react/jsx-props-no-spreading
        keycloak={{
          initialized: true,
          authenticated: true,
        }}
      />
    );
  };

  return withKeycloak;
});

jest.mock('components/pagination/withPagination', () => {
  const withPagination = (Component) => {
    return (props) => (
      <Component
        {...props} // eslint-disable-line react/jsx-props-no-spreading
        pagination={{
          onChangeItemsPerPage: () => {},
          onChangeCurrentPage: () => {},
        }}
      />
    );
  };

  return withPagination;
});

describe('DogTableContainer', () => {
  const errorMessageKey = 'error.dataLoading';

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('calls API', async () => {
    apiDogsGet.mockImplementation(() => Promise.resolve({ dogs: dogMocks, count: 2 }));
    const { queryByText } = render(<DogTableContainer />);

    await wait(() => {
      expect(apiDogsGet).toHaveBeenCalledTimes(1);
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
    });
  });

  it('shows an error if the API call is not successful', async () => {
    apiDogsGet.mockImplementation(() => {
      throw new Error();
    });
    const { getByText } = render(<DogTableContainer />);

    wait(() => {
      expect(apiDogsGet).toHaveBeenCalledTimes(1);
      expect(getByText(errorMessageKey)).toBeInTheDocument();
    });
  });
});
