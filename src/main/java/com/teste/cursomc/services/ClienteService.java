package com.teste.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.teste.cursomc.domain.Cidade;
import com.teste.cursomc.domain.Cliente;
import com.teste.cursomc.domain.Endereco;
import com.teste.cursomc.domain.enums.Perfil;
import com.teste.cursomc.domain.enums.TipoCliente;
import com.teste.cursomc.dto.ClienteDTO;
import com.teste.cursomc.dto.ClienteNewDTO;
import com.teste.cursomc.repositories.CidadeRepository;
import com.teste.cursomc.repositories.ClienteRepository;
import com.teste.cursomc.repositories.EnderecoRepository;
import com.teste.cursomc.security.UserSS;
import com.teste.cursomc.services.exceptions.AuthorizationException;
import com.teste.cursomc.services.exceptions.DataIntegrityException;
import com.teste.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;

	
	@Value("${img.folder.client.profile}")
	private String folder;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.size.client.profile}")
	private int profileSize;
	
	@Value("${img.thumb.size.client.profile}")
	private int profileThumbSize;
	
	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user == null || (!user.hasRole(Perfil.ADMIN) && !id.equals(user.getId()))) {
			throw new AuthorizationException("Acesso negado");
		}
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getSimpleName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há pedidos relacionados");
		}

	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || (!user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername()))) {
			throw new AuthorizationException("Acesso negado");
		}
		Cliente obj = repo.findByEmail(email);
		if(obj==null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getSimpleName());
		}
		return obj;
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cidade = cidadeRepository.findById(objDto.getCidadeId()).orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + objDto.getCidadeId() + ", Tipo: " + Cidade.class.getName()));
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, cidade);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpegImage = imageService.getJpgImageFromFile(multipartFile);
		jpegImage = imageService.cropSquare(jpegImage);
		
//		BufferedImage thumbJpegImage = imageService.resize(jpegImage, profileThumbSize);
		
		jpegImage = imageService.resize(jpegImage, profileSize);
		
		String fileName = prefix+user.getId()+".jpg";
		
		//s3Service.uploadFile(imageService.getInputStream(thumbJpegImage, "jpg"), folder+"/thumb/"+fileName, "image");
		
		return s3Service.uploadFile(imageService.getInputStream(jpegImage, "jpg"), fileName, "image");
		
	}

}
