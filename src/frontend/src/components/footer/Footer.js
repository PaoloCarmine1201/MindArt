const Footer = () => {
    return (
        <footer className="bg-dark text-white py-3 mt-5">
            <div className="container text-center">
                <p>&copy; 2024 Your Company. All rights reserved.</p>
                <ul className="list-inline">
                    <li className="list-inline-item">
                        <Link to="/privacy" className="text-white">Privacy Policy</Link>
                    </li>
                    <li className="list-inline-item">
                        <Link to="/terms" className="text-white">Terms of Service</Link>
                    </li>
                </ul>
            </div>
        </footer>
    );
};

export default Footer;
