import React from 'react';
import '@testing-library/jest-dom/extend-expect';
import { render } from '@testing-library/react';

import 'components/__mocks__/i18n';
import DogDetails from 'components/DogDetails';
import dogMock from 'components/__mocks__/dogMocks';

describe('DogDetails component', () => {
  test('renders data in details widget', () => {
    const { getByText } = render(<DogDetails dog={dogMock} />);

    expect(getByText('entities.dog.name')).toBeInTheDocument();
  });
});
