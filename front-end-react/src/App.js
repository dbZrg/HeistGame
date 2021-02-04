import './App.css';
import Navigation from './components/Navigation'
import { Switch, Route, Link, BrowserRouter as Router } from 'react-router-dom';
import MemberComponent from './components/MemberComp';
import MembersComponent from './components/MembersComp';
import SkillsComponent from './components/SkillsComp';
import HeistComponent from './components/HeistComp';
import HeistsComponent from './components/HeistsComp';
import HeistMembersComponent from './components/HeistMembersComp'
import HeistSkillsComponent from './components/HeistSkillsComp'
import HeistStatusComponent  from './components/HeistStatusComp'
import HeistOutcomeComponent from './components/HeistOutcomeComp'

function App() {
    return (
      <>
      <Router>
      <Navigation />
        <Switch>
          <Route path={['/members']} component={MembersComponent}/>
          <Route path={['/member/:id/skills']} component={SkillsComponent}/>
          <Route path={['/member/:id']} component={MemberComponent}/>
          <Route path={['/heists']} component={HeistsComponent}/>
          <Route path={['/heist/:id/members']} component={HeistMembersComponent}/>
          <Route path={['/heist/:id/skills']} component={HeistSkillsComponent}/>
          <Route path={['/heist/:id/status']} component={HeistStatusComponent}/>
          <Route path={['/heist/:id/outcome']} component={HeistOutcomeComponent}/>
          <Route path={['/heist/:id']} component={HeistComponent}/>
         
        </Switch>
    </Router>
    </>
    );
  }


export default App;
