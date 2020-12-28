import React from 'react';
import { fireEvent, render, wait } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { apiDogGet, apiDogPut } from 'api/dogs';
import DogEditFormContainer from 'components/DogEditFormContainer';
import 'i18n/__mocks__/i18nMock';
import dogMock from 'components/__mocks__/dogMocks';

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

describe('DogEditFormContainer', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const errorMessageKey = 'error.dataLoading';
  const successMessageKey = 'common.dataSaved';

  const onErrorMock = jest.fn();
  const onUpdateMock = jest.fn();

  it('loads data', async () => {
    apiDogGet.mockImplementation(() => Promise.resolve(dogMock));
    const { queryByText } = render(
      <DogEditFormContainer id="1" onError={onErrorMock} onUpdate={onUpdateMock} />
    );

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
      expect(apiDogGet).toHaveBeenCalledWith('', '1');
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
      expect(onErrorMock).toHaveBeenCalledTimes(0);
    });
  }, 7000);

  it('saves data', async () => {
    apiDogGet.mockImplementation(() => Promise.resolve(dogMock));
    apiDogPut.mockImplementation(() => Promise.resolve(dogMock));

    const { findByTestId, queryByText } = render(
      <DogEditFormContainer id="1" onError={onErrorMock} onUpdate={onUpdateMock} />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiDogPut).toHaveBeenCalledTimes(1);
      expect(apiDogPut).toHaveBeenCalledWith('', dogMock);
      expect(queryByText(successMessageKey)).toBeInTheDocument();
      expect(onErrorMock).toHaveBeenCalledTimes(0);
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
    });
  }, 7000);

  it('shows an error if data is not successfully loaded', async () => {
    apiDogGet.mockImplementation(() => Promise.reject());
    const { queryByText } = render(
      <DogEditFormContainer id="1" onError={onErrorMock} onUpdate={onUpdateMock} />
    );

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
      expect(apiDogGet).toHaveBeenCalledWith('', '1');
      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(queryByText(errorMessageKey)).toBeInTheDocument();
      expect(queryByText(successMessageKey)).not.toBeInTheDocument();
    });
  }, 7000);

  it('shows an error if data is not successfully saved', async () => {
    apiDogGet.mockImplementation(() => Promise.resolve(dogMock));
    apiDogPut.mockImplementation(() => Promise.reject());
    const { findByTestId, getByText } = render(
      <DogEditFormContainer id="1" onError={onErrorMock} />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiDogGet).toHaveBeenCalledTimes(1);
      expect(apiDogGet).toHaveBeenCalledWith('', '1');

      expect(apiDogPut).toHaveBeenCalledTimes(1);
      expect(apiDogPut).toHaveBeenCalledWith('', dogMock);

      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(getByText(errorMessageKey)).toBeInTheDocument();
    });
  }, 7000);
});
