import PropTypes from 'prop-types';

const conferenceType = PropTypes.shape({
  id: PropTypes.number,

  street: PropTypes.string,
});

export default conferenceType;
