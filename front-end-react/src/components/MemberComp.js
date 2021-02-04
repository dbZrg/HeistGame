import React from 'react';
import MemberService from '../services/MembersService';

class MemberComp extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            member: null,
            
        }
    }

    componentDidMount(){
        MemberService.getMember(this.props.match.params.id)
            .then(response => this.setState({member: response.data}))
            .catch(function(error) {
            console.log('Fetch error: ' + error.message);    
             
            })
        }

    render (){
        const member = this.state.member;
        if (member === null) {
            return <span>TEXT</span>
        }


        return (
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <td> Member name</td>
                            <td> Member email</td>
                            <td> Skills</td>
                        </tr>
                    </thead>
                    <tbody>
                            <tr>
                                <td>{member.name}</td>
                                <td>{member.email}</td>
                                <td><ul>
                                    {member.skills.map(sub =>
                                        <li>
                                            {sub.name}
                                            {sub.level}
                                        </li>
                                    )}

                                </ul>
                                </td>
                            </tr>
                    </tbody>

                </table>
            </div>

        )
    }

}

export default MemberComp