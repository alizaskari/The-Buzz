import { useState } from 'react';
import logo from './logo.svg';
import './App.css';
import Ideas from './components/IdeaRows.js';
import CreateIdea from './components/CreateIdea.js';

// function ReactBase() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }

/**
 * The app component containing all other components (a CreateIdea component and an IdeaRows component)
 * @param {*} props All properties and objects to send to the components of the app. In this case, the list of ideas/messages to display are included 
 * @returns the App component, containing a CreateIdea component, followed by an IdeaRows component below it
 */
function App(props) {

  const backendUrl = "https://team-git-gud.dokku.cse.lehigh.edu/";

  // const ideasState = useState([
  //   { mId: 1, mLikes: 1, mMessage: "test message 1" },
  //   { mId: 2, mLikes: 10, mMessage: "test message 2" },
  //   { mId: 3, mLikes: 100, mMessage: "test message 3" },
  // ]);

  //console.log("making ideas");

  const ideasState = useState(props.ideas);

  const [ideas, setIdeas] = ideasState;



  return (
    <>
      <div className="Banner">
        <h1>The Buzz</h1>
      </div>
      <div>
        <CreateIdea state={ideasState} />
      </div>
      <div className="ideaRows">
        <Ideas ideas={ideas} />
      </div>
    </>
  );
}

export default App;

