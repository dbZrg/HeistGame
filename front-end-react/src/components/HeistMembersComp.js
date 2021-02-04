import React from 'react';
import HeistService from '../services/HeistService';

class HeistMembersComp extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            members:[]
        }
    }

    componentDidMount(){
        HeistService.getHeistMembers(this.props.match.params.id).then((response) =>{
            this.setState({members: response.data})
        });
    }

    render (){
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
                        {
                            this.state.members.map(
                                member =>
                                <tr key= {member.email}>
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
                            )
                        }
                    </tbody>

                </table>
            </div>

        )
    }

}

export default HeistMembersComp