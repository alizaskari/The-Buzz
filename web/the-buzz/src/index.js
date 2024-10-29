import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

var ideasList = [];

init();

/**
 * Prepares the page for creation by sending a GET request to /messages to get the list of ideas to display from the backend
 */
function init() {
  const doAjax = async () => {
    await fetch(`/messages`, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json; charset=UTF-8'
      }
    }).then((response) => {
      // If we get an "ok" message, clear the form
      if (response.ok) {
        return Promise.resolve(response.json());
      }
      // Otherwise, handle server errors with a detailed popup message
      else {
        window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
      }
      return Promise.reject(response);
    }).then((data) => {
      ideasList = loadIdeas(data);
      createApp();
      console.log(data);
    }).catch((error) => {
      console.warn('Something went wrong.', error);
      window.alert("Unspecified error");
    });
  }

  doAjax().then(console.log).catch(console.log);
}

/**
 * Loads the list of ideas received from the backend into an array
 * @param {*} data The list of ideas to display from the backend
 * @returns The list of ideas to display, prepared and formatted for the frontend
 */
function loadIdeas(data) {
  let ideas = []
  console.log("loading ideas...");
  for (let i = 0; i < data.mData.length; i++) {
    ideas[i] = {};
    ideas[i].mId = data.mData[i].mId;
    ideas[i].mLikes = data.mData[i].mLikes;
    ideas[i].mMessage = data.mData[i].mMessage;
  }
  console.log("ideas loaded!");
  return ideas;
}

/**
 * Creates the React root and renders the App component
 */
function createApp() {
  const root = ReactDOM.createRoot(document.getElementById('root'));
  root.render(
    <React.StrictMode>
      <App ideas={ideasList} />
    </React.StrictMode>
  );
}


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
//reportWebVitals();
