import PropTypes from 'prop-types';

const dogType = PropTypes.shape({
  id: PropTypes.number,

  name: PropTypes.string,
});

export default dogType;
