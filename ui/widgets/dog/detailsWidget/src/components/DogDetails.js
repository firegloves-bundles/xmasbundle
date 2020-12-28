import React from 'react';
import PropTypes from 'prop-types';
import { withTranslation } from 'react-i18next';
import Box from '@material-ui/core/Box';

import dogType from 'components/__types__/dog';
import DogFieldTable from 'components/dog-field-table/DogFieldTable';

const DogDetails = ({ t, dog }) => {
  return (
    <Box>
      <h3>
        {t('common.widgetName', {
          widgetNamePlaceholder: 'Dog',
        })}
      </h3>
      <DogFieldTable dog={dog} />
    </Box>
  );
};

DogDetails.propTypes = {
  dog: dogType,
  t: PropTypes.func.isRequired,
};

DogDetails.defaultProps = {
  dog: {},
};

export default withTranslation()(DogDetails);
