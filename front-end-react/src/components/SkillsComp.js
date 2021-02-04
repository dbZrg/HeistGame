import React from 'react';
import MemberService from '../services/MembersService';

class Skills extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            skills: null,
            
        }
    }

    componentDidMount(){
        MemberService.getMemberSkills(this.props.match.params.id)
            .then(response => this.setState({skills: response.data}))
            .catch(function(error) {
            console.log('Fetch error: ' + error.message);    
             
            })
        }

    render (){
        const skillz = this.state.skills;
        if (skillz === null) {
            return <span>TEXT</span>
        }


        return (
            <div>
                <h3>Main skill: {skillz.mainSkill}</h3>
                <h3>Skills:</h3>

                <ul>
                {skillz.skills.map(sub =>
                    <li>
                        {sub.name}
                        {sub.level}
                    </li>
                )}
                </ul>
            </div>

        )
    }

}

export default Skills