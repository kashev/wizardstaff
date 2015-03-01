/**
 * ajax options. There are various properties with url being the only required property.
 * @typedef ajaxOptions
 * @property {string} [method='get'] - The HTTP method to use: 'get', 'post', 'put', 'delete', 'options',
 *    or any other standard method supported by the running environment.
 * @property {string} url - The URL to make the ajax request to. e.g. 'http://www.example.com?name=value'
 * @property {object} [data] - The request body, mainly to be used in combination with 'post' or 'put'.
 *    e.g. { username: 'guest' }
 * @property {object} headers - Custom HTTP headers. Specify additional headers.
 *    e.g. { 'x-extra': 'Extra Header' }
 * @property {boolean} [async=true] - Whether the request will be asynchronous.
 *    Specify false for a blocking, synchronous request.
 */
var httpClient = function(opt, success, failure) {
	//opt.url: url to call
	//opt.method: 'POST', 'GET', 'UPDATE', etc
	//opt.data: { 'username': 'myUsername'}
	//opt.heaaders: { 'Authorization': 'Token: 0sdknweeksokdf0'}
	//opt.async: true or false

};
