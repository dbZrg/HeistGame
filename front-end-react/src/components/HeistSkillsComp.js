import React from 'react';
import HeistService from '../services/HeistService';

class Skills extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            skills: [],
            
        }
    }

    componentDidMount(){
        HeistService.getHeistSkills(this.props.match.params.id)
            .then(response => this.setState({skills: response.data}))
            .catch(function(error) {
            console.log('Fetch error: ' + error.message);    
             
            })
        }

    render (){
        const skills = this.state.skills;
        if (skills === null) {
            return <span>TEXT</span>
        }


        return (
            <div>
                
                <h3>Skills:</h3>

                <ul>
                {skills.map(sub =>
                    <li>
                        {sub.name}
                        {sub.level}
                        {sub.members}
                    </li>
                )}
                </ul>
            </div>

        )
    }

}

export default Skills