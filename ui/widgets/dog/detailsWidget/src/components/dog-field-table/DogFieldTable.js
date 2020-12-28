import React from 'react';
import PropTypes from 'prop-types';
import { withTranslation } from 'react-i18next';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

import dogType from 'components/__types__/dog';

const DogFieldTable = ({ t, dog }) => (
  <Table>
    <TableHead>
      <TableRow>
        <TableCell>{t('common.name')}</TableCell>
        <TableCell>{t('common.value')}</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      <TableRow>
        <TableCell>
          <span>{t('entities.dog.id')}</span>
        </TableCell>
        <TableCell>
          <span>{dog.id}</span>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell>
          <span>{t('entities.dog.name')}</span>
        </TableCell>
        <TableCell>
          <span>{dog.name}</span>
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
);

DogFieldTable.propTypes = {
  dog: dogType,
  t: PropTypes.func.isRequired,
};

DogFieldTable.defaultProps = {
  dog: [],
};

export default withTranslation()(DogFieldTable);
