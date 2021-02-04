import React from 'react';
import HeistService from '../services/HeistService';

class HeistsComp extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            heists:[]
        }
    }

    componentDidMount(){
        HeistService.getAll().then((response) =>{
            this.setState({heists: response.data})
        });
    }

    render (){
        return (
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <td> Heist name</td>
                            <td> Location</td>
                            <td> Skills</td>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.state.heists.map(
                                heist =>
                                <tr key= {heist.name}>
                                    <td>{heist.name}</td>
                                    <td>{heist.location}</td>
                                    <td><ul>
                                        {heist.skills.map(sub =>
                                            <li>
                                                {sub.name}
                                                {sub.level}
                                                {sub.membets}
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

export default HeistsComp