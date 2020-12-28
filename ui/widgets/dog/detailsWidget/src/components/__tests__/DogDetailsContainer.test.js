import React from 'react';
import { render, wait } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';

import 'components/__mocks__/i18n';
import { apiDogGet } from 'api/dog';
import dogApiGetResponseMock from 'components/__mocks__/dogMocks';
import DogDetailsContainer from 'components/DogDetailsContainer';

jest.mock('api/dog');

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

beforeEach(() => {
  apiDogGet.mockClear();
});

describe('DogDetailsContainer component', () => {
  test('requests data when component is mounted', async () => {
    apiDogGet.mockImplementation(() => Promise.resolve(dogApiGetResponseMock));

    render(<DogDetailsContainer id="1" />);

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
    });
  });

  test('data is shown after mount API call', async () => {
    apiDogGet.mockImplementation(() => Promise.resolve(dogApiGetResponseMock));

    const { getByText } = render(<DogDetailsContainer id="1" />);

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
      expect(getByText('entities.dog.name')).toBeInTheDocument();
    });
  });

  test('error is shown after failed API call', async () => {
    const onErrorMock = jest.fn();
    apiDogGet.mockImplementation(() => Promise.reject());

    const { getByText } = render(<DogDetailsContainer id="1" onError={onErrorMock} />);

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(getByText('common.couldNotFetchData')).toBeInTheDocument();
    });
  });
});
