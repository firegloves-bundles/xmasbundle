import React from 'react';
import '@testing-library/jest-dom/extend-expect';
import { fireEvent, render } from '@testing-library/react';
import 'components/__mocks__/i18n';
import conferenceMocks from 'components/__mocks__/conferenceMocks';
import ConferenceTable from 'components/ConferenceTable';

describe('ConferenceTable', () => {
  it('shows conferences', () => {
    const { getByText } = render(<ConferenceTable items={conferenceMocks} />);
    expect(
      getByText(
        'Tempore in eaque et. Maiores cumque ut enim et illum voluptatem nam quibusdam qui. Aut ratione omnis dignissimos quo tempora quo nihil. Nisi voluptatum amet libero veniam voluptatem qui. Maiores laboriosam expedita sint quos.'
      )
    ).toBeInTheDocument();
    expect(
      getByText(
        'Et et nostrum voluptas sapiente. Delectus impedit omnis maiores est beatae ab cupiditate ipsa. Qui omnis dolorem dolor harum dolorum deserunt. A aliquam at commodi.'
      )
    ).toBeInTheDocument();
  });

  it('shows no conferences message', () => {
    const { queryByText } = render(<ConferenceTable items={[]} />);
    expect(
      queryByText(
        'Tempore in eaque et. Maiores cumque ut enim et illum voluptatem nam quibusdam qui. Aut ratione omnis dignissimos quo tempora quo nihil. Nisi voluptatum amet libero veniam voluptatem qui. Maiores laboriosam expedita sint quos.'
      )
    ).not.toBeInTheDocument();
    expect(
      queryByText(
        'Et et nostrum voluptas sapiente. Delectus impedit omnis maiores est beatae ab cupiditate ipsa. Qui omnis dolorem dolor harum dolorum deserunt. A aliquam at commodi.'
      )
    ).not.toBeInTheDocument();

    expect(queryByText('entities.conference.noItems')).toBeInTheDocument();
  });

  it('calls onSelect when the user clicks a table row', () => {
    const onSelectMock = jest.fn();
    const { getByText } = render(
      <ConferenceTable items={conferenceMocks} onSelect={onSelectMock} />
    );
    fireEvent.click(
      getByText(
        'Tempore in eaque et. Maiores cumque ut enim et illum voluptatem nam quibusdam qui. Aut ratione omnis dignissimos quo tempora quo nihil. Nisi voluptatum amet libero veniam voluptatem qui. Maiores laboriosam expedita sint quos.'
      )
    );
    expect(onSelectMock).toHaveBeenCalledTimes(1);
  });
});
