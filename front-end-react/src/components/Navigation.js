import React from "react";
import {Link} from 'react-router-dom';

function Navigation(){
    return(
        <div className="navbar">
            <ul className="nav-menu-items">
                <li>
                    <Link to="/members">
                        <span>Members</span>
                    </Link>
                </li>
                <li>
                    <Link to="/heists">
                        <span>Heists</span>
                    </Link>
                </li>
            </ul>
        </div>

    );

}

export default Navigation;