import PropTypes from 'prop-types';

export default PropTypes.shape({
  id: PropTypes.number,

  street: PropTypes.string,
});

export const formValues = PropTypes.shape({
  street: PropTypes.string,
});

export const formTouched = PropTypes.shape({
  street: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
});

export const formErrors = PropTypes.shape({
  street: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
});
