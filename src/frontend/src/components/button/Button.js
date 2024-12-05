const Button = ({ type, label, onClick }) => {
    return (
        <button className={`btn btn-${type}`} onClick={onClick}>
            {label}
        </button>
    );
};

export default Button;
