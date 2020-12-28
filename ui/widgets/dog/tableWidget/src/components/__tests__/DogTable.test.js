import React from 'react';
import '@testing-library/jest-dom/extend-expect';
import { fireEvent, render } from '@testing-library/react';
import 'components/__mocks__/i18n';
import dogMocks from 'components/__mocks__/dogMocks';
import DogTable from 'components/DogTable';

describe('DogTable', () => {
  it('shows dogs', () => {
    const { getByText } = render(<DogTable items={dogMocks} />);
    expect(
      getByText(
        'Rerum velit natus et consequatur qui delectus. Eum iure fuga ratione et deserunt modi cupiditate. Corporis dolores sit voluptatem. Nisi cum voluptas distinctio quos minus quia.'
      )
    ).toBeInTheDocument();
    expect(
      getByText(
        'Et libero explicabo aliquam asperiores veritatis praesentium dolorem totam. Neque rem non quisquam ut blanditiis corporis. Odio minus dicta expedita sit tempore tenetur. Voluptas ut sed ipsum animi dolorum reiciendis voluptas vel. Harum fugiat laborum quisquam.'
      )
    ).toBeInTheDocument();
  });

  it('shows no dogs message', () => {
    const { queryByText } = render(<DogTable items={[]} />);
    expect(
      queryByText(
        'Rerum velit natus et consequatur qui delectus. Eum iure fuga ratione et deserunt modi cupiditate. Corporis dolores sit voluptatem. Nisi cum voluptas distinctio quos minus quia.'
      )
    ).not.toBeInTheDocument();
    expect(
      queryByText(
        'Et libero explicabo aliquam asperiores veritatis praesentium dolorem totam. Neque rem non quisquam ut blanditiis corporis. Odio minus dicta expedita sit tempore tenetur. Voluptas ut sed ipsum animi dolorum reiciendis voluptas vel. Harum fugiat laborum quisquam.'
      )
    ).not.toBeInTheDocument();

    expect(queryByText('entities.dog.noItems')).toBeInTheDocument();
  });

  it('calls onSelect when the user clicks a table row', () => {
    const onSelectMock = jest.fn();
    const { getByText } = render(<DogTable items={dogMocks} onSelect={onSelectMock} />);
    fireEvent.click(
      getByText(
        'Rerum velit natus et consequatur qui delectus. Eum iure fuga ratione et deserunt modi cupiditate. Corporis dolores sit voluptatem. Nisi cum voluptas distinctio quos minus quia.'
      )
    );
    expect(onSelectMock).toHaveBeenCalledTimes(1);
  });
});
