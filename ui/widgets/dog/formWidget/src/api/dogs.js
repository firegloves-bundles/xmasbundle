import { getDefaultOptions, request } from 'api/helpers';

const resource = 'dogs';

export const apiDogGet = async (serviceUrl, id) => {
  const url = `${serviceUrl}/${resource}/${id}`;
  const options = {
    ...getDefaultOptions(),
    method: 'GET',
  };
  return request(url, options);
};

export const apiDogPost = async (serviceUrl, dog) => {
  const url = `${serviceUrl}/${resource}`;
  const options = {
    ...getDefaultOptions(),
    method: 'POST',
    body: dog ? JSON.stringify(dog) : null,
  };
  return request(url, options);
};

export const apiDogPut = async (serviceUrl, dog) => {
  const url = `${serviceUrl}/${resource}`;
  const options = {
    ...getDefaultOptions(),
    method: 'PUT',
    body: dog ? JSON.stringify(dog) : null,
  };
  return request(url, options);
};

export const apiDogDelete = async (serviceUrl, id) => {
  const url = `${serviceUrl}/${resource}/${id}`;
  const options = {
    ...getDefaultOptions(),
    method: 'DELETE',
  };
  return request(url, options);
};
